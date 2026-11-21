from fastapi import APIRouter, Depends, status
from sqlalchemy.orm import Session
from typing import List
from app.schemas.nutrition import FoodCreate,FoodOut
from app.database.session import get_db
from app.services.nutrition_service import create_custom_food,search_foods
router_nutri=APIRouter(prefix="/nutrition",tags=["nutrition"])

@router_nutri.get('/',response_model=List[FoodOut])
async def get_food(db:Session=Depends(get_db),query:str=""):
    return await search_foods(db=db,query=query)
