from fastapi import APIRouter, Depends, status
from sqlalchemy.orm import Session
from typing import List

from app.database.session import get_db
from app.services.exercise_service import CreateExercise, get_all_exercises, get_exercise_id, update_data_service
from app.schemas.exercise import ExerciseCreate, ExerciseResponse, ExerciseUpdate

router = APIRouter(
    prefix="/exercises",
    tags=["Exercises"]
)

@router.post("/", response_model=ExerciseResponse, status_code=status.HTTP_201_CREATED)
async def create_exercise(exercise_data: ExerciseCreate, db: Session = Depends(get_db)):
    return await CreateExercise(db=db, Exercise_data=exercise_data)

@router.get("/", response_model=List[ExerciseResponse])
async def get_exercises(skip: int = 0, limit: int = 20, db: Session = Depends(get_db)):
    return await get_all_exercises(db=db, skip=skip, limit=limit)

@router.get("/{exercise_id}", response_model=ExerciseResponse)
async def get_exercise_by_id(exercise_id: int, db: Session = Depends(get_db)):
    return await get_exercise_id(db=db, exercise_id=exercise_id)

@router.put("/{exercise_id}", response_model=ExerciseResponse)
async def update_data(exercise_id: int, exercise_data: ExerciseUpdate, db: Session = Depends(get_db)):
    return await update_data_service(db=db, exercise_data=exercise_data, exercise_id=exercise_id)