import secrets
import hashlib

import jwt

from datetime import datetime, timedelta, timezone

from pwdlib import PasswordHash
from app.core.config import settings



# Password Hashing
password_hash = PasswordHash.recommended()


def hash_password(password: str) -> str:

    return password_hash.hash(password)


def verify_password(
    plain_password: str,
    hashed_password: str
) -> bool:

    return password_hash.verify(
        plain_password,
        hashed_password
    )


# Access Token Generation
def create_access_token(user_id: int) -> str:

    now = datetime.now(timezone.utc)

    expire = now + timedelta(
        minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES
    )

    payload = {
        "sub": str(user_id),
        "type": "access",
        "iat": now,
        "exp": expire
    }

    token = jwt.encode(
        payload,
        settings.SECRET_KEY,
        algorithm=settings.ALGORITHM
    )

    return token


# Refresh Token Generation
def create_refresh_token() -> str:

    return secrets.token_urlsafe(48)


# Refresh Token Hashing
def hash_refresh_token(token: str) -> str:

    return hashlib.sha256(
        token.encode("utf-8")
    ).hexdigest()


# Refresh Token Expiry
def get_refresh_token_expiry():

    return datetime.now(timezone.utc) + timedelta(
        days=settings.REFRESH_TOKEN_EXPIRE_MINUTES
    )