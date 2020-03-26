# ----------------------------------------------------------------------------
# ffmpeg -i "source.mp4" -ss 00:02.000 -t 00:04.568 -c copy "folder/target.mp4"
# ----------------------------------------------------------------------------

echo "Executing ffmpeg..."

startTime=$4
endTime=$6
targetFileName=$9

targetDirectory=`dirname "$targetFileName"`
mkdir -p "$targetDirectory"
chmod a+rwx "$targetDirectory"
echo "Created directory: $targetDirectory"

echo "$startTime - $endTime" >"$targetFileName"
chmod a+rwx "$targetFileName"
echo "Created file: $targetFileName"