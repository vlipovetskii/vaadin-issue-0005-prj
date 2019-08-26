for /f "delims=" %%f in ('dir vlfsoft.*.springboot.app /b /ad') do set CurrSpringbootAppName=%%f
call gradlew -q %CurrSpringbootAppName%:dependencies > %CurrSpringbootAppName%_dependencies_report.log
pause
