data "azurerm_resource_group" "lapauseclope" {
  name     = "LaPauseClope"
}

resource "tls_private_key" "lapauseclope" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "azurerm_virtual_network" "lapauseclope" {
  name                = "lapauseclope-network"
  address_space       = ["10.0.0.0/16"]
  location            = var.location
  resource_group_name = data.azurerm_resource_group.lapauseclope.name
}

resource "azurerm_subnet" "lapauseclope" {
  name                 = "internal"
  resource_group_name  = data.azurerm_resource_group.lapauseclope.name
  virtual_network_name = azurerm_virtual_network.lapauseclope.name
  address_prefixes     = ["10.0.2.0/24"]
}

resource "azurerm_public_ip" "lapauseclope" {
  name                = "lapauseclope-public-ip"
  location            = var.location
  resource_group_name = data.azurerm_resource_group.lapauseclope.name
  allocation_method   = "Static"
  sku                 = "Basic"
}

resource "azurerm_network_interface" "lapauseclope" {
  name                = "lapauseclope-nic"
  location            = var.location
  resource_group_name = data.azurerm_resource_group.lapauseclope.name

  ip_configuration {
    name                          = "internal"
    subnet_id                     = azurerm_subnet.lapauseclope.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.lapauseclope.id
  }
}

resource "azurerm_linux_virtual_machine" "lapauseclope" {
  name                = "lapauseclope-machine"
  resource_group_name = data.azurerm_resource_group.lapauseclope.name
  location            = var.location
  size                = "Standard_F2"
  admin_username      = "adminuser"
  network_interface_ids = [
    azurerm_network_interface.lapauseclope.id,
  ]

  admin_ssh_key {
    username   = "adminuser"
    public_key = tls_private_key.lapauseclope.public_key_openssh
  }

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-jammy"
    sku       = "22_04-lts"
    version   = "latest"
  }
}

resource "azurerm_network_security_group" "lapauseclope" {
  name                = "lapauseclope-nsg"
  location            = var.location
  resource_group_name = data.azurerm_resource_group.lapauseclope.name

  security_rule {
    name                       = "AllowSSH"
    priority                   = 1001
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "22"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }
}

resource "azurerm_subnet_network_security_group_association" "lapauseclope" {
  subnet_id                 = azurerm_subnet.lapauseclope.id
  network_security_group_id = azurerm_network_security_group.lapauseclope.id
}
