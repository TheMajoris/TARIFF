resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "TARIFF-app-rds-subnet"
  subnet_ids = var.subnet_ids
}


resource "aws_db_instance" "rds" {
  engine                 = "postgres"
  instance_class         = "db.t3.micro"
  allocated_storage      = 20
  name                   = "TARIFFdb"
  username               = var.db_username
  password               = var.db_password
  db_subnet_group_name   = aws_db_subnet_group.rds_subnet_group.name
  vpc_security_group_ids = var.sg_ids
  skip_final_snapshot    = true
  publicly_accessible    = false
}


output "db_endpoint" { value = aws_db_instance.rds.address }
