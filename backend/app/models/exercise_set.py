from sqlalchemy import (
    Column,
    Integer,
    Float,
    ForeignKey
)

from sqlalchemy.orm import relationship

from app.database.session import Base


class ExerciseSet(Base):

    __tablename__ = "exercise_sets"

    id = Column(
        Integer,
        primary_key=True,
        index=True
    )

    session_id = Column(
        Integer,
        ForeignKey("workout_sessions.id", ondelete="CASCADE"),
        nullable=False,
        index=True
    )

    exercise_id = Column(
        Integer,
        ForeignKey("exercises.id"),
        nullable=False,
        index=True
    )

    set_number = Column(
        Integer,
        nullable=False
    )

    weight = Column(
        Float,
        nullable=False
    )

    reps = Column(
        Integer,
        nullable=False
    )

    rir = Column(
        Integer,
        nullable=True
    )

    workout_session = relationship(
        "WorkoutSession",
        back_populates="exercise_sets"
    )

    exercise = relationship(
        "Exercise"
    )