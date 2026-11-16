from sqlalchemy import (
    Column,
    Integer,
    ForeignKey
)

from sqlalchemy.orm import relationship

from app.database.session import Base


class WorkoutExercise(Base):

    __tablename__ = "workout_exercises"

    id = Column(
        Integer,
        primary_key=True,
        index=True
    )

    workout_id = Column(
        Integer,
        ForeignKey("workouts.id", ondelete="CASCADE"),
        nullable=False,
        index=True
    )

    exercise_id = Column(
        Integer,
        ForeignKey("exercises.id"),
        nullable=False,
        index=True
    )

    sets = Column(
        Integer,
        nullable=False
    )

    min_reps = Column(
        Integer,
        nullable=False
    )

    max_reps = Column(
        Integer,
        nullable=False
    )

    rest_seconds = Column(
        Integer,
        nullable=False
    )

    order = Column(
        Integer,
        nullable=False
    )

    workout = relationship(
        "Workout",
        back_populates="workout_exercises"
    )

    exercise = relationship(
        "Exercise"
    )