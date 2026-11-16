from sqlalchemy import (
    Column,
    Integer,
    String,
    Text,
    DateTime,
    ForeignKey
)

from sqlalchemy.sql import func
from sqlalchemy.orm import relationship

from app.database.session import Base


class WorkoutSession(Base):

    __tablename__ = "workout_sessions"

    id = Column(
        Integer,
        primary_key=True,
        index=True
    )

    user_id = Column(
        Integer,
        ForeignKey("users.id", ondelete="CASCADE"),
        nullable=False,
        index=True
    )

    workout_id = Column(
        Integer,
        ForeignKey("workouts.id", ondelete="SET NULL"),
        nullable=True,
        index=True
    )

    status = Column(
        String(30),
        default="in_progress",
        nullable=False
    )

    started_at = Column(
        DateTime(timezone=True),
        server_default=func.now(),
        nullable=False
    )

    completed_at = Column(
        DateTime(timezone=True),
        nullable=True
    )

    notes = Column(
        Text,
        nullable=True
    )

    user = relationship(
        "User",
        back_populates="workout_sessions"
    )

    workout = relationship(
        "Workout",
        back_populates="sessions"
    )

    exercise_sets = relationship(
        "ExerciseSet",
        back_populates="workout_session"
    )