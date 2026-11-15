from fastapi import FastAPI
from fastapi import APIRouter, Depends, status
from app.database.session import get_db
from sqlalchemy.orm import Session
router_auth = APIRouter(
    prefix="/auth",
    tags=["Authentication"]
)
@router_auth.post('/register')
def register(db:Session=Depends(get_db)):
    return {"message":"register end point created successfully"
    }