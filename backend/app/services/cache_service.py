import json

from app.core.redis import redis_client


async def get_cache(key: str):

    data = await redis_client.get(key)

    if data is None:
        return None

    return json.loads(data)


async def set_cache(
    key: str,
    value,
    expire: int = 300
):

    await redis_client.set(
        key,
        json.dumps(value),
        ex=expire
    )

async def delete_cache_data(pattern:str):
    cursor=0
    while True:
        cursor,keys=await redis_client.scan(
        cursor=cursor,
        match=pattern,
        count=100

        )
        if keys:
            await redis_client.delete(*keys)
        if cursor==0:
            break
async def delete_cache(key: str):

    await redis_client.delete(key)