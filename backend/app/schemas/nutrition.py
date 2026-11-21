from pydantic import BaseModel, Field
from typing import Optional


class FoodCreate(BaseModel):
    name: str = Field(..., min_length=1, max_length=200)
    category: str = Field(default="General", max_length=100)

    calories: float = Field(..., ge=0)
    protein: float = Field(..., ge=0)
    carbohydrates: float = Field(..., ge=0)
    fats: float = Field(..., ge=0)
    fibre: float = Field(default=0.0, ge=0)

    base_unit: str = Field(default="g", max_length=10)


class FoodOut(FoodCreate):
    id: int
    source: str
    created_by: Optional[int] = None

    class Config:
        from_attributes = True
