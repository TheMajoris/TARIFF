output "alb_dns_name" { value = module.alb.alb_dns_name }
output "rds_endpoint" { value = module.rds.db_endpoint }
output "frontend_ecr_repo" { value = var.frontend_image_uri }
output "backend_ecr_repo" { value = var.backend_image_uri }

