# Route53 Zone

# VPC
module "vpc" {
  source = "./modules/vpc"
  name = var.name

  vpc_cidr = var.vpc_cidr

  public_subnets = var.public_subnets
  private_subnets = var.private_subnets
}

# Bastion & NAT EC2
module "server" {
  source = "./modules/ec2"
  name = var.name

  vpc_id = module.vpc.vpc_id
  subnet_id = module.vpc.public_subnets_ids[0]

  inbound_rules = var.ec2_inbound_rule
  outbound_rules = var.common_outbound_rule

  key_name = var.key_pair_name
  key_pair_file = file("~/.ssh/${var.key_pair_name}.pub")

  depends_on = [ module.vpc ]
}

# RDS
module "db" {
  source = "./modules/rds"
  name = var.name

  vpc_id = module.vpc.vpc_id
  az_name = var.az_names[0]
  private_subnet_ids = module.vpc.private_subnets_ids

  inbound_rule = var.db_inbound_rule
  outbound_rule = var.common_outbound_rule

  database_name = var.db_name
  database_user = var.db_username
  database_password = var.db_password
}
