# Public EC2
#   - Security Group
#   - Instance Profile(Role)
#   - Key Pair
#   - EIP

# EC2 설정
module "ec2_sg" {
  source = "../security_group"
  name = "${var.name}-public-ec2-sg"
  vpc_id = var.vpc_id
  inbound_rules = var.inbound_rules
  outbound_rules = var.outbound_rules
}

module "ec2_profile" {
  source = "./profile"
  name = var.name
}

resource "aws_key_pair" "ec2_key_pair" {
  key_name = var.key_name
  public_key = var.key_pair_file
}

# EC2 생성
resource "aws_instance" "public_ec2" {
  ami = "ami-0e4a9ad2eb120e054" # Amazon Linux2(ap-northeast-2)
  instance_type = "t2.micro" # 프리티어

  subnet_id = var.subnet_id
  vpc_security_group_ids = [
    module.ec2_sg.id
  ]

  key_name = aws_key_pair.ec2_key_pair.key_name
  user_data = file("${path.module}/user_data.sh")
  iam_instance_profile = module.ec2_profile.ec2_profile

  source_dest_check = false

  tags = {
    Name = "${var.name}-public-ec2"
  }
}

# EIP 
resource "aws_eip" "ec2_eip" {
  vpc = true
  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_eip_association" "ec2_eip_association" {
  allocation_id = aws_eip.ec2_eip.id
  instance_id = aws_instance.public_ec2.id
}