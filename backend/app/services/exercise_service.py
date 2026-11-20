from sqlalchemy.orm import Session
from sqlalchemy import func
from app.services.cache_service import delete_cache
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
    if new_exercise.get("name"):
        new_exercise["name"] = new_exercise["name"].strip().lower()

    for key in ["primary_muscle", "equipment", "exercise_type"]:
        if new_exercise.get(key):
            new_exercise[key] = new_exercise[key].strip().lower()

    if new_exercise.get("secondary_muscles"):
        new_exercise["secondary_muscles"] = [muscle.strip().lower() for muscle in new_exercise["secondary_muscles"]]

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
async def get_exercise_id(db:Session,exercise_id:int):
    cache_key=f"exercise:{exercise_id}"
    cache_data=await get_cache(cache_key)
    if cache_data is not None:
        return cache_data
    exercise_data=db.query(Exercise).filter(Exercise.id==exercise_id,Exercise.is_active==True).first()
    if not exercise_data:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Exercise not found"
            )
    result=ExerciseResponse.model_validate(exercise_data).model_dump(mode="json")
    await set_cache(
        cache_key,
        result,
        expire=300

    )
    return result
async def update_data_service(exercise_data:ExerciseUpdate,db:Session,exercise_id:int):
    exercise=db.query(Exercise).filter(Exercise.id==exercise_id,Exercise.is_active==True).first()
    if not exercise:

            raise HTTPException(

                status_code=status.HTTP_404_NOT_FOUND,

                detail="Exercise not found"

            )
    update_data_model=exercise_data.model_dump(exclude_unset=True)
    for key,value in update_data_model.items():
        if key in ["name" ,"primary_muscle", "equipment", "exercise_type"] and value:
            value=value.strip().lower()
        elif key == "secondary_muscles" and value:
            value=[muscle.strip().lower() for muscle in value]
        setattr(exercise,key,value)
    try:
        db.commit()
        db.refresh(exercise)
    except IntegrityError:

            db.rollback()
            raise HTTPException(

                status_code=status.HTTP_409_CONFLICT,

                detail="Exercise violates a database constraint"

            )

    except:
        db.rollback()
        raise
    try:
        await delete_cache_data(f"exercises:all:*")
        await delete_cache(f"exercise:{exercise_id}")
    except Exception:
        pass

    return exercise
async def delete_exercise(db: Session, exercise_id: int):

    exercise = db.query(Exercise).filter(
        Exercise.id == exercise_id
    ).first()

    if not exercise:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Exercise not found"
        )

    try:
        db.delete(exercise)
        db.commit()

    except Exception:
        db.rollback()
        raise

    try:
        await delete_cache_data("exercises:all:*")
        await delete_cache(f"exercise:{exercise_id}")

    except Exception as e:
        print(f"Cache invalidation failed: {e}")

    return {
        "message": f"Exercise {exercise_id} deleted successfully"
    }
        