
# EC2
output "server_sg_id" {
  value = module.server.ec2_sg_id
}

output "server_ip" {
  value = module.server.ec2_ip
}

# RDS
output "db_endpoint" {
  value = module.db.rds_endpoint
}