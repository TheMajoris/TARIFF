module "vpc" {
	source = "./vpc"
}

module "fargate_frontend" {
  source    = "./fargate"
  vpc_id    = module.vpc.vpc_id
  subnet_ids = [module.vpc.public_subnet_ids[0]]
  service_name = "frontend"
  container_port = 5173
  image_uri = var.frontend_image_uri
  alb_arn = module.alb.alb_arn
  target_group_name = "frontend-tg"
}

module "fargate_backend" {
  source    = "./fargate"
  vpc_id    = module.vpc.vpc_id
  subnet_ids = [module.vpc.public_subnet_ids[1]] 
  service_name = "backend"
  container_port = 8080
  image_uri = var.backend_image_uri
  alb_arn = module.alb.alb_arn
  target_group_name = "backend-tg"
  path_pattern = "/api/*"
}

module "rds" {
  source       = "./rds"
  db_username  = var.db_username
  db_password  = var.db_password
  subnet_ids   = [module.vpc.private_subnet_id] 
  sg_ids        = module.fargate_backend.sg_id 
}

module "alb" {
  source = "./alb"
  vpc_id = module.vpc.vpc_id
  public_subnet_ids = module.vpc.public_subnet_ids
}
