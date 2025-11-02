variable "aws_region" { default = "ap-southeast-1" }
variable "project_name" { default = "TARIFF" }
variable "db_username" {}
variable "db_password" { sensitive = true }
variable "frontend_image_uri" {}
variable "backend_image_uri" {}
