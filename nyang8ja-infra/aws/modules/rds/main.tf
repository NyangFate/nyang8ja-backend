# Main RDS
#   - Security Group
#   - Subnet Gruop
#   - Parameter Gruop

module "db_sg" {
  source = "../security_group"
  name = "${var.name}-rds-sg"
  vpc_id = var.vpc_id
  inbound_rules = var.inbound_rule
  outbound_rules = var.outbound_rule
}

resource "aws_db_subnet_group" "db_subnet_group" {
  name = "${var.name}-db-subnet-group"
  subnet_ids = var.private_subnet_ids
}

resource "aws_db_parameter_group" "db_parameter_group" {
  name   = "${var.name}-db-pg"
  family = "mysql8.0"

  # Boolean 값이 Bit로 들어가는 문제 해결
  parameter {
    name  = "log_bin_trust_function_creators"
    value = "1"
  }

  # Timezone 즉시 변경 설정
  parameter {
    name  = "time_zone"
    value = "Asia/Seoul"
    apply_method = "immediate"
  }
}

resource "aws_db_instance" "db" {
  depends_on = [ 
    module.db_sg,
    aws_db_subnet_group.db_subnet_group,
    aws_db_parameter_group.db_parameter_group
  ]

  identifier = "${var.name}-rds"
  vpc_security_group_ids = [ module.db_sg.id ]
  availability_zone = var.az_name
  db_subnet_group_name = aws_db_subnet_group.db_subnet_group.name

  # 기본 데이터 인코딩 - utf8mb4
  # 프리티어 스펙
  engine = "mysql"
  engine_version = "8.0.35"
  instance_class = "db.t4g.micro"
  
  db_name              = var.database_name
  username             = var.database_user
  password             = var.database_password

  max_allocated_storage = 1000
  allocated_storage = 20
  backup_retention_period = 5 # 백업본 저장기간
  storage_encrypted = true

  copy_tags_to_snapshot = true
  skip_final_snapshot = true

  parameter_group_name = aws_db_parameter_group.db_parameter_group.name
  apply_immediately = true
}