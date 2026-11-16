from fastapi import APIRouter, Depends, status
from sqlalchemy.orm import Session
from typing import List

from app.database.session import get_db
from app.services.exercise_service import CreateExercise, get_all_exercises
from app.schemas.exercise import ExerciseCreate, ExerciseResponse

router = APIRouter(
    prefix="/exercises",
    tags=["Exercises"]
)

@router.post("/", response_model=ExerciseResponse, status_code=status.HTTP_201_CREATED)
def create_exercise(exercise_data: ExerciseCreate, db: Session = Depends(get_db)):
    return CreateExercise(db=db, Exercise_data=exercise_data)

@router.get("/", response_model=List[ExerciseResponse])
def get_exercises(skip: int = 0, limit: int = 20, db: Session = Depends(get_db)):
    return get_all_exercises(db=db, skip=skip, limit=limit)