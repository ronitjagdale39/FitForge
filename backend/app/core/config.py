import os
from pydantic_settings import BaseSettings, SettingsConfigDict

env_path = os.path.join(os.path.dirname(os.path.dirname(os.path.dirname(__file__))), ".env")

class Settings(BaseSettings):
    DATABASE_URL: str
    ALGORITHM: str
    SECRET_KEY: str
    ACCESS_TOKEN_EXPIRE_MINUTES: int
    REFRESH_TOKEN_EXPIRE_MINUTES: int
    REDIS_URL:str
    OTP_EXPIRY :int
    SMTP_HOST:str
    SMTP_PORT:int
    SMTP_USER:str
    SMTP_PASSWORD:str
    GEMINI_API_KEY:str

    model_config = SettingsConfigDict(
        env_file=env_path,
        extra="ignore"
    )

settings = Settings()
