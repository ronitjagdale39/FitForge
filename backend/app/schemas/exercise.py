from pydantic import BaseModel, Field, ConfigDict
from typing import Optional


class ExerciseCreate(BaseModel):

    name: str = Field(min_length=2, max_length=150)

    description: Optional[str] = None

    primary_muscle: str

    secondary_muscles: list[str] = []

    equipment: str

    exercise_type: str

    instructions: Optional[str] = None


class ExerciseUpdate(BaseModel):

    name: Optional[str] = None

    description: Optional[str] = None

    primary_muscle: Optional[str] = None

    secondary_muscles: Optional[list[str]] = None

    equipment: Optional[str] = None

    exercise_type: Optional[str] = None

    instructions: Optional[str] = None


class ExerciseResponse(BaseModel):

    id: int

    name: str

    description: Optional[str]

    primary_muscle: str

    secondary_muscles: list[str]

    equipment: str

    exercise_type: str

    is_active: bool

    model_config = ConfigDict(from_attributes=True)