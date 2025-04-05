resource "aws_ecr_repository" "ecr" {
  name                 = var.name
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}

resource "aws_ecr_lifecycle_policy" "ecr_policy" {
  repository = aws_ecr_repository.ecr.name

  policy = <<EOF
{
  "rules": [
    {
      "rulePriority": 1,
      "description": "Keep last 3 images",
      "selection": {
        "tagStatus": "untagged",
        "countType": "imageCountMoreThan",
        "countNumber": 3
      },
      "action": {
        "type": "expire"
      }
    }
  ]
}
EOF
}