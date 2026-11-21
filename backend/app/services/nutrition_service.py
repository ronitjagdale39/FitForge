from sqlalchemy.orm import Session
from app.services.cache_service import get_cache,set_cache
from app.models.food import Food
from app.schemas.nutrition import FoodCreate,FoodOut


async def search_foods(db: Session, query: str = "", limit: int=20):
    cache_key=f"food:all:{query.lower()}:{limit}"
    cache_data=await get_cache(cache_key)
    if cache_data is not None:
        return cache_data
    foods = db.query(Food)

    if query:
        foods = foods.filter(Food.name.ilike(f"%{query}%"))
    result=[FoodOut.model_validate(f).model_dump(mode="json") for f in foods]
    await set_cache(
    cache_key,
    result,
    expire=300
    )

    return foods.limit(limit).all()


async def create_custom_food(db: Session, user_id: int, food_data: FoodCreate):
    new_food = Food(
        **food_data.model_dump(),
        source="CUSTOM",
        created_by=user_id
    )

    db.add(new_food)
    db.commit()
    db.refresh(new_food)
    return new_food
