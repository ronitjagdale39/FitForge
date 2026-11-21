from sqlalchemy import (
    Column,
    Integer,
    String,
    Float,
    ForeignKey,
    DateTime
)
from sqlalchemy.sql import func

from app.database.session import Base


class Food(Base):

    __tablename__ = "foods"

    id = Column(Integer, primary_key=True, index=True)

    name = Column(String(200), nullable=False, index=True)

    category = Column(String(100), nullable=False, default="General")

    # Nutritional values per 100g or 100ml
    calories = Column(Float, nullable=False)
    protein = Column(Float, nullable=False)
    carbohydrates = Column(Float, nullable=False)
    fats = Column(Float, nullable=False)
    fibre = Column(Float, nullable=False, default=0.0)

    base_unit = Column(String(10), nullable=False, default="g")       # "g" or "ml"
    source = Column(String(20), nullable=False, default="CUSTOM")     # "USDA", "IFCT", "CUSTOM"

    # Null for global foods (USDA/IFCT); stores user.id if a user creates a custom food
    created_by = Column(
        Integer,
        ForeignKey("users.id", ondelete="CASCADE"),
        nullable=True
    )

    created_at = Column(
        DateTime(timezone=True),
        server_default=func.now(),
        nullable=False
    )
