locals {
  location = lookup(var.Regions, var.loc, "UK South")
  tags     = var.static_tags
}
