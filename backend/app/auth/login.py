from fastapi import FastAPI
from fastapi import APIRouter, Depends, status
from app.database.session import get_db
from sqlalchemy.orm import Session
from app.services.auth_login import login_user
from app.schemas.user import LoginRequest
def Login(data:LoginRequest,db:Session=Depends(get_db)):
    return login_user(db=db,email=data.email,password=data.password)