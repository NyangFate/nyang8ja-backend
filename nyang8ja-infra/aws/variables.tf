variable "name" { }

// VPC
variable "vpc_cidr" { }
variable "az_names" { }
variable "public_subnets" { }
variable "private_subnets" { }

// Security Rule
variable "ec2_inbound_rule" { }
variable "db_inbound_rule" { }
variable "common_outbound_rule" { }

// EC2
variable "key_pair_name" { }

// RDS
variable "db_name" { }
variable "db_username" { }
variable "db_password" { }
