output "ec2_sg_id" {
  value = module.ec2_sg.id
}

output "ec2_id" {
  value = aws_instance.public_ec2.id
}

output "ec2_host_id" {
  value = aws_instance.public_ec2.host_id
}

output "ec2_ip" {
  value = aws_eip.ec2_eip.public_ip
}

output "key_pair_name" {
  value = aws_key_pair.ec2_key_pair.key_name
}