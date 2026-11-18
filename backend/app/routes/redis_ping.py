from fastapi import APIRouter

from app.core.redis import redis_client

router_redis = APIRouter()


@router_redis.get("/redis")
async def redis_health():

    try:
        await redis_client.ping()

        return {
            "status": "connected",
            "database": "redis"
        }

    except Exception:
        return {
            "status": "disconnected",
            "database": "redis"
        }
@router_redis.get("/redis-test")
async def redis_test():

    await redis_client.set("fitforge:test", "Connected")

    value = await redis_client.get("fitforge:test")

    return {
        "stored_value": value
    }