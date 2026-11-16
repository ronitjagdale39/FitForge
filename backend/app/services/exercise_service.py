from sqlalchemy.orm import Session
from sqlalchemy import func
from sqlalchemy.exc import IntegrityError
from fastapi import HTTPException, status
from app.models.exercise import Exercise
from app.schemas.exercise import (
    ExerciseCreate,
    ExerciseUpdate
)

# creating exercises
def CreateExercise(db: Session, Exercise_data: ExerciseCreate):
    existing_exercise = db.query(Exercise).filter(
        func.lower(Exercise.name) == Exercise_data.name.strip().lower()
    ).first()

    if existing_exercise:
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT,
            detail="Exercise already exists"
        )

    new_exercise = Exercise_data.model_dump()
    new_exercise["name"] = new_exercise["name"].strip()
    new_exercise_data = Exercise(**new_exercise)

    try:
        db.add(new_exercise_data)
        db.commit()
        db.refresh(new_exercise_data)
    except IntegrityError:
        db.rollback()
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT,
            detail="Exercise already exists or violates a database constraint"
        )
    return new_exercise_data

# get all exercises
def get_all_exercises(
    db: Session,
    skip: int = 0,
    limit: int = 20
):
    exercises = db.query(Exercise).filter(
        Exercise.is_active == True
    ).offset(skip).limit(limit).all()
    return exercises