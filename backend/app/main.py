from fastapi import FastAPI, APIRouter
from app.routes.auth import router as auth_router
from app.routes.exercises import router as exercises_router
from app.database.session import engine, Base
from app.models import user,register,workout,exercise,workout_session,workout,workout_exercise,exercise_set # ensure models are imported to register with Base
from app.routes.redis_ping import router_redis

# Create tables if they don't exist
Base.metadata.create_all(bind=engine)



app = FastAPI(
    title="FitForge AI",
    description="AI-powered Personal Fitness Platform",
    version="1.0.0"
)

app.include_router(auth_router)
app.include_router(exercises_router)
app.include_router(router_redis)
# API Version 1
api_router = APIRouter(prefix="/api/v1")


@api_router.get("/")
def root():

    return {
        "message": "Welcome to FitForge AI",
        "status": "running",
        "version": "1.0.0"
    }


@api_router.get("/health")
def health_check():

    return {
        "status": "healthy",
        "application": "FitForge AI"
    }


app.include_router(api_router)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)