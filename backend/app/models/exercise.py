from sqlalchemy import (
    Column,
    Integer,
    String,
    Boolean,
    DateTime,
    JSON,
    Text
)

from sqlalchemy.sql import func
from sqlalchemy.orm import relationship
from app.database.session import Base


class Exercise(Base):

    __tablename__ = "exercises"

    id = Column(
        Integer,
        primary_key=True,
        index=True
    )

    name = Column(
        String(150),
        nullable=False,
        index=True
    )

    description = Column(
        Text,
        nullable=True
    )

    primary_muscle = Column(
        String(50),
        nullable=False
    )

    secondary_muscles = Column(
        JSON,
        nullable=False,
        default=list
    )

    equipment = Column(
        String(100),
        nullable=False
    )

    exercise_type = Column(
        String(50),
        nullable=False
    )

    instructions = Column(
        Text,
        nullable=True
    )

    is_active = Column(
        Boolean,
        default=True,
        nullable=False
    )

    created_at = Column(
        DateTime(timezone=True),
        server_default=func.now(),
        nullable=False
    )