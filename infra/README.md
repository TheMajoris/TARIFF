for my aws architecture, i have:
1. public subnet: AWS Fargate: SvelteKit Frontend
2. public subnet: AWS Fargate: Spring Boot Backend
3. private subnet: RDS for postgresSQL

The Fargate should pull images from AWS ECS, because i want my images to be pushed in via github actions on AWS ECR. Note that this is just a school project, does not have to be production ready. it just needs to be reachable from a public website.


## Future improvements:
-  github actions workflow to build and push images to ECR repos 
-  change backend to private subnet and NAT gateway to perplexity
