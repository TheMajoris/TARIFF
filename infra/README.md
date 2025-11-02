1. VPC module creates public & private subnets and exports vpc_id, public_subnet_ids, private_subnet_id.
2. Fargate modules deploy ECS services in respective public subnets and register tasks with ALB target groups.
3. Single ALB module handles routing: default to frontend, path /api/* to backend.
4. RDS module creates a PostgreSQL DB in the private subnet accessible only from backend SG.
5. This setup avoids NAT Gateway since both Fargate tasks are public.
6. For school/demo project, one private subnet for RDS is sufficient.

## Future improvements:
-  github actions workflow to build and push images to ECR repos 
-  change backend to private subnet and NAT gateway to perplexity
