from sqlalchemy.orm import Session
from sqlalchemy import func
from sqlalchemy.exc import IntegrityError
from fastapi import HTTPException, status
from app.models.exercise import Exercise
from app.schemas.exercise import (
    ExerciseCreate,
    ExerciseUpdate,
    ExerciseResponse
)
from app.services.cache_service import get_cache, set_cache, delete_cache_data

# creating exercises
async def CreateExercise(db: Session, Exercise_data: ExerciseCreate):
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

    for key in ["primary_muscle", "equipment", "exercise_type"]:
        if new_exercise.get(key):
            new_exercise[key] = new_exercise[key].lower()

    if new_exercise.get("secondary_muscles"):
        new_exercise["secondary_muscles"] = [muscle.lower() for muscle in new_exercise["secondary_muscles"]]

    new_exercise_data = Exercise(**new_exercise)

    try:
        db.add(new_exercise_data)
        db.commit()
        db.refresh(new_exercise_data)
        await delete_cache_data("exercises:all:*")
    except IntegrityError:
        db.rollback()
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT,
            detail="Exercise already exists or violates a database constraint"
        )
    return new_exercise_data

# get all exercises
async def get_all_exercises(
    db: Session,
    skip: int = 0,
    limit: int = 20
):
    cache_key = f"exercises:all:{skip}:{limit}"
    cache_data = await get_cache(cache_key)
    if cache_data is not None:
        return cache_data

    exercises = db.query(Exercise).filter(
        Exercise.is_active == True
    ).offset(skip).limit(limit).all()

    result = [ExerciseResponse.model_validate(exercise).model_dump(mode="json")
              for exercise in exercises]

    await set_cache(
        cache_key,
        result,
        expire=300
    )

    return result