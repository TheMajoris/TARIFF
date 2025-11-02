resource "aws_lb" "alb" {
  name               = "TARIFF-alb"
  internal           = false
  load_balancer_type = "application"
  subnets            = var.public_subnet_ids
}


output "alb_arn" { value = aws_lb.alb.arn }
output "alb_dns_name" { value = aws_lb.alb.dns_name }


