import secrets
from fastapi import APIRouter, BackgroundTasks, Depends
from app.services.email_service import send_otp_email
from app.core.config import settings
from sqlalchemy.orm import Session
from app.models.user import User
from fastapi import HTTPException, status
from app.services.cache_service import set_cache, get_cache, delete_cache
from app.schemas.otp import OTPRequest,OTPVerify

def generate_otp() -> str:
    return f"{secrets.randbelow(1000000):06d}"


async def save_otp(email: str) -> str:
    otp = generate_otp()
    key = f"otp:{email.lower()}"
    await set_cache(key, otp, settings.OTP_EXPIRY)
    return otp


async def verify_otp(email: str, otp: str) -> bool:
    key = f"otp:{email.lower()}"
    stored_otp = await get_cache(key)

    if not stored_otp:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="OTP expired or not found"
        )

    if str(stored_otp) != str(otp):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Invalid OTP"
        )

    await delete_cache(key)
    return True
async def send_otp(db:Session,data:OTPRequest,background_task:BackgroundTasks):
    user=db.query(User).filter(User.email==data.email.lower()).first()
    if not user:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="User with this email not found"
            )

    if user.is_verified:
            return {"message": "Email is already verified"}
    otp=await save_otp(data.email)
    background_task.add_task(send_otp_email, data.email, otp)
    return {
        "message": "OTP sent successfully. Please check your email."
    }
async def verify_user_otp(data: OTPVerify, db: Session ):
    user = db.query(User).filter(User.email == data.email.lower()).first()
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found"
        )

    await verify_otp(data.email, data.otp)

    user.is_verified = True
    db.commit()
    db.refresh(user)

    return {
        "message": "Email verified successfully",
        "email": user.email,
        "is_verified": user.is_verified
    }

