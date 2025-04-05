output "rds_endpoint" {
  value = aws_db_instance.db.endpoint
}

output "rds_domain" {
  value = aws_db_instance.db.domain
}