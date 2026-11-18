import redis.asyncio as redis

from app.core.config import settings


redis_client = redis.from_url(
    settings.REDIS_URL,
    encoding="utf-8",
    decode_responses=True
)


async def connect_redis():
    await redis_client.ping()


async def close_redis():
    await redis_client.aclose()