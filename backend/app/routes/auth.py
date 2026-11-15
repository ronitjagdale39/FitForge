from fastapi import APIRouter, Depends, status
from sqlalchemy.orm import Session
from fastapi import FastAPI
from fastapi import APIRouter, Depends, status
from app.database.session import get_db
from sqlalchemy.orm import Session
from app.services.auth_login import login_user
from app.database.session import get_db
from app.services.auth_service import register_user
from app.schemas.user import LoginRequest
from app.schemas.user import (
    UserRegister,
    RegisterResponse
)

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
def Login(data:LoginRequest,db:Session=Depends(get_db)):
    return login_user(db=db,email=data.email,password=data.password)