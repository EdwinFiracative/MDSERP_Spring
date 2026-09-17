# To run in PowerShell, save this script as dockerdeploy.ps1 and run it in the terminal with the command ./dockerdeploy.ps1

# Set in a variable the Docker context to the remote Linux server where the container will be deployed.
$ctx = "linux-mds-server"

# Stop and remove the existing container if it exists.
docker --context $ctx rm -f mdsback-container 2>$null | Out-Null

# Remove the previous image if it exists.
docker --context $ctx rmi  mdserp:1.0.0 2>$null | Out-Null

# Build the new image from the current Dockerfile.
docker --context $ctx build -t  mdserp:1.0.0 .

# Run the container in detached mode and restart it automatically unless stopped manually.
# The logs volume persists application logs on the host across container rebuilds.
docker --context $ctx run -d --name mdsback-container --restart unless-stopped -p 8181:8181 -v mdserp-logs:/app/logs mdserp:1.0.0