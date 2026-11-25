import os
from email.message import EmailMessage
import aiosmtplib
from app.core.config import settings



async def send_otp_email(to_email: str, otp_code: str) -> None:
    if not settings.SMTP_USER or not settings.SMTP_PASSWORD:
        raise ValueError("SMTP_USER or SMTP_PASSWORD is missing in .env")

    msg = EmailMessage()
    msg["From"] = f"FitForge AI <{settings.SMTP_USER}>"
    msg["To"] = to_email
    msg["Subject"] = "Your FitForge AI Verification Code"

    # Plain-text fallback
    msg.set_content(f"Your FitForge AI verification code is: {otp_code}. Valid for 5 minutes.")

    # HTML version
    html_content = f"""
    <div style="font-family: Arial, sans-serif; padding: 20px; color: #1f2937;">
        <h2 style="color: #4F46E5;">FitForge AI Email Verification</h2>
        <p>Your 6-digit verification code is:</p>
        <div style="font-size: 28px; font-weight: bold; letter-spacing: 6px; color: #111827; margin: 16px 0;">
            {otp_code}
        </div>
        <p style="font-size: 13px; color: #6b7280;">This code will expire in <strong>5 minutes</strong>. Do not share it with anyone.</p>
    </div>
    """
    msg.add_alternative(html_content, subtype="html")

    await aiosmtplib.send(
        msg,
        hostname=settings.SMTP_HOST,
        port=settings.SMTP_PORT,
        start_tls=True,
        username=settings.SMTP_USER,
        password=settings.SMTP_PASSWORD,
    )
