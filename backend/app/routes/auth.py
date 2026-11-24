from fastapi import APIRouter, Depends, status, BackgroundTasks
from sqlalchemy.orm import Session

from app.database.session import get_db
from app.services.auth_login import login_user
from app.services.auth_service import register_user
from app.services.otp_service import send_otp, verify_user_otp

from app.schemas.user import LoginRequest
from app.schemas.user import UserRegister, RegisterResponse
from app.schemas.otp import OTPRequest, OTPVerify

router = APIRouter(
    prefix="/auth",
    tags=["Authentication"]
)


@router.post(
    "/register",
    response_model=RegisterResponse,
    status_code=status.HTTP_201_CREATED
)
def register(
    user_data: UserRegister,
    db: Session = Depends(get_db)
):
    result = register_user(
        db=db,
        name=user_data.name,
        email=user_data.email,
        password=user_data.password
    )
    return result

@router.post('/login')
def Login(data: LoginRequest, db: Session = Depends(get_db)):
    return login_user(db=db, email=data.email, password=data.password)

@router.post('/send-otp')
async def send_otp_endpoint(data: OTPRequest, background_tasks: BackgroundTasks, db: Session = Depends(get_db)):
    return await send_otp(db=db, data=data, background_task=background_tasks)

@router.post('/verify-otp')
async def verify_otp_endpoint(data: OTPVerify, db: Session = Depends(get_db)):
    return await verify_user_otp(data=data, db=db)