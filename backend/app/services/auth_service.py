from sqlalchemy.orm import Session
from sqlalchemy import select

from fastapi import HTTPException, status

from app.models.user import User
from app.models.register import RefreshToken

from app.core.security import (
    hash_password,
    create_access_token,
    create_refresh_token,
    hash_refresh_token,
    get_refresh_token_expiry
)


def register_user(
    db: Session,
    name: str,
    email: str,
    password: str
):

    # 1. Check existing user

    existing_user = db.scalar(
        select(User).where(User.email == email)
    )

    if existing_user:
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT,
            detail="Email already registered"
        )

    try:

        # 2. Hash password

        hashed_password = hash_password(password)

        # 3. Create user

        new_user = User(
            name=name,
            email=email,
            password_hash=hashed_password
        )

        db.add(new_user)

        # Generate user ID

        db.flush()

        # 4. Generate access token

        access_token = create_access_token(
            user_id=new_user.id
        )

        # 5. Generate refresh token

        refresh_token = create_refresh_token()

        # 6. Hash refresh token

        refresh_token_hash = hash_refresh_token(
            refresh_token
        )

        # 7. Store refresh token

        new_refresh_token = RefreshToken(
            user_id=new_user.id,
            token_hash=refresh_token_hash,
            expires_at=get_refresh_token_expiry()
        )

        db.add(new_refresh_token)

        # 8. Commit transaction

        db.commit()

        db.refresh(new_user)

        return {
            "message": "User registered successfully",
            "user": new_user,
            "tokens": {
                "access_token": access_token,
                "refresh_token": refresh_token,
                "token_type": "bearer"
            }
        }

    except Exception:
        db.rollback()
        raise