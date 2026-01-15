resource "aws_s3_bucket" "email_bucket" {
  bucket = "simple-blog-email-bucket"
}

resource "aws_sns_topic" "email_topic" {
  name = "simple-blog-email-topic"
}

resource "aws_ses_email_identity" "from_email" {
  email = "no-reply@localstack.local"
}