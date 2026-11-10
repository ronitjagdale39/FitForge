from pydantic import BaseModel, EmailStr, Field, ConfigDict


# Registration Request
class UserRegister(BaseModel):

    name: str = Field(min_length=2, max_length=100)

    email: EmailStr

    password: str = Field(min_length=8, max_length=128)


# User Response
class UserResponse(BaseModel):

    id: int
    name: str
    email: EmailStr

    model_config = ConfigDict(from_attributes=True)


# Token Response
class TokenResponse(BaseModel):

    access_token: str
    refresh_token: str
    token_type: str = "bearer"


# Registration Response
class RegisterResponse(BaseModel):

    message: str
    user: UserResponse
    tokens: TokenResponse