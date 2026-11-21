from app.database.session import SessionLocal
from app.models.food import Food

SAMPLE_FOODS = [
    {"name": "Soya Chunks (Raw)", "category": "Protein", "calories": 345.0, "protein": 52.0, "carbohydrates": 33.0, "fats": 0.5, "fibre": 13.0, "base_unit": "g"},
    {"name": "Paneer", "category": "Dairy", "calories": 265.0, "protein": 18.3, "carbohydrates": 1.2, "fats": 20.8, "fibre": 0.0, "base_unit": "g"},
    {"name": "Wheat Chapati", "category": "Grains", "calories": 297.0, "protein": 9.8, "carbohydrates": 61.0, "fats": 1.7, "fibre": 9.0, "base_unit": "g"},
    {"name": "Cooked White Rice", "category": "Grains", "calories": 130.0, "protein": 2.7, "carbohydrates": 28.0, "fats": 0.3, "fibre": 0.4, "base_unit": "g"},
    {"name": "Toned Milk", "category": "Dairy", "calories": 58.0, "protein": 3.0, "carbohydrates": 4.7, "fats": 3.0, "fibre": 0.0, "base_unit": "ml"},
    {"name": "Dahi (Curd)", "category": "Dairy", "calories": 60.0, "protein": 3.1, "carbohydrates": 4.4, "fats": 3.2, "fibre": 0.0, "base_unit": "g"},
    {"name": "Raw Rajma", "category": "Legumes", "calories": 333.0, "protein": 22.9, "carbohydrates": 60.6, "fats": 1.3, "fibre": 15.2, "base_unit": "g"},
    {"name": "Rolled Oats", "category": "Grains", "calories": 379.0, "protein": 13.2, "carbohydrates": 67.7, "fats": 6.5, "fibre": 10.1, "base_unit": "g"},
    {"name": "Roasted Groundnuts", "category": "Nuts", "calories": 567.0, "protein": 25.8, "carbohydrates": 16.1, "fats": 49.2, "fibre": 8.5, "base_unit": "g"},
    {"name": "Banana", "category": "Fruits", "calories": 89.0, "protein": 1.1, "carbohydrates": 22.8, "fats": 0.3, "fibre": 2.6, "base_unit": "g"},
    {"name": "Besan (Chickpea Flour)", "category": "Legumes", "calories": 387.0, "protein": 22.0, "carbohydrates": 58.0, "fats": 6.0, "fibre": 10.8, "base_unit": "g"},
    {"name": "Soaked Matki (Moth Beans)", "category": "Legumes", "calories": 343.0, "protein": 23.0, "carbohydrates": 61.5, "fats": 1.6, "fibre": 14.0, "base_unit": "g"},
]


def seed_foods():
    db = SessionLocal()
    added_count = 0

    try:
        for item in SAMPLE_FOODS:
            existing = db.query(Food).filter(Food.name == item["name"]).first()
            if not existing:
                food = Food(
                    name=item["name"],
                    category=item["category"],
                    calories=item["calories"],
                    protein=item["protein"],
                    carbohydrates=item["carbohydrates"],
                    fats=item["fats"],
                    fibre=item["fibre"],
                    base_unit=item["base_unit"],
                    source="IFCT",
                    created_by=None
                )
                db.add(food)
                added_count += 1

        db.commit()
        print(f"Done! Added {added_count} new food items.")
    except Exception as e:
        db.rollback()
        print("Error seeding foods:", e)
    finally:
        db.close()


if __name__ == "__main__":
    seed_foods()
