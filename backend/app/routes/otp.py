from fastapi import APIRouter, Depends,BackgroundTasks
from sqlalchemy.orm import Session

from app.database.session import get_db
from app.schemas.otp import OTPRequest, OTPVerify
from app.services.otp_service import send_otp, verify_user_otp

otp_route = APIRouter(prefix="/otp", tags=["otp"])


@otp_route.post("/send_otp")
async def send_otp_route(data: OTPRequest,background_task:BackgroundTasks, db: Session = Depends(get_db)):
    return await send_otp(db=db, data=data,background_task=background_task)


@otp_route.post("/verify_otp")
async def verify_otp_route(data: OTPVerify, db: Session = Depends(get_db)):
    return await verify_user_otp(db=db, data=data)
