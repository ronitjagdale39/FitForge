from sqlalchemy.orm import Session
from sqlalchemy import select

from fastapi import HTTPException, status

from app.models.user import User
from app.models.register import RefreshToken

from app.core.security import (
    verify_password,
    create_access_token,
    create_refresh_token,
    hash_refresh_token,
    get_refresh_token_expiry
)


def login_user(
    db: Session,
    email: str,
    password: str
):

    # 1. Find user

    user = db.scalar(
        select(User).where(User.email == email)
    )

    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid email or password"
        )

    # Check if user is verified
    if not user.is_verified:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Email not verified. Please verify your email first."
        )

    # 2. Verify password

    if not verify_password(password, user.password_hash):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid email or password"
        )

    try:

        # 3. Generate access token

        access_token = create_access_token(
            user_id=user.id
        )

        # 4. Generate refresh token

        refresh_token = create_refresh_token()

        # 5. Hash refresh token

        token_hash = hash_refresh_token(
            refresh_token
        )

        # 6. Store refresh token

        new_refresh_token = RefreshToken(
            user_id=user.id,
            token_hash=token_hash,
            expires_at=get_refresh_token_expiry()
        )

        db.add(new_refresh_token)

        # 7. Commit

        db.commit()

        return {
            "message": "Login successful",
            "user": user,
            "tokens": {
                "access_token": access_token,
                "refresh_token": refresh_token,
                "token_type": "bearer"
            }
        }

    except Exception:
        db.rollback()
        raise