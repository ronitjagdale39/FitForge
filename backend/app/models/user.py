from sqlalchemy import (
    Column,
    Integer,
    String,
    Boolean,
    DateTime,
    text
)
from sqlalchemy.sql import func
from sqlalchemy.orm import relationship

from app.database.session import Base


class User(Base):

    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)

    name = Column(String(100), nullable=False)

    email = Column(
        String(255),
        unique=True,
        index=True,
        nullable=False
    )

    password_hash = Column(String(255), nullable=False)

    is_active = Column(Boolean, default=True, nullable=False)

    created_at = Column(
        DateTime(timezone=True),
        server_default=func.now(),
        nullable=False
    )
    is_verified = Column(
            Boolean,
            nullable=False,
            default=False,
            server_default=text('false')
    )
    refresh_tokens = relationship(
        "RefreshToken",
        back_populates="user",
        cascade="all, delete-orphan"
    )
    workouts = relationship(
        "Workout",
        back_populates="user",
        cascade="all, delete-orphan"
    )
    workout_sessions=relationship(
        "WorkoutSession",
        back_populates="user"
    )