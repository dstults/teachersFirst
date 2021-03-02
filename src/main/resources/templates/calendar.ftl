<!DOCTYPE html>
<#include "head.ftl">
<body>
<#include "header.ftl">
	<div class="calendar-explanation">
		<p>Calendar of all your scheduled appointments</p>
	</div>

	<div class="calendar">
		<div class="col dates-col">
			<div class="content">
				<h2 class="year">2021</h2>
				<h3 class="month">February</h3>
				<table>
					<tr class="weekdays">
						<th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th>
					</tr>
					<script>
						document.write('<tr class="days">');
						for( var _i = 1; _i <= 28; _i += 1 ){
							var _addClass = '';
							if( _i % 7 == 0 ){
								document.write('<td><a href="#" title="'+_i+'" data-value="'+_i+'">'+_i+'</a></td></tr>');
								document.write('<tr class-"days">');
							}
							else {
								document.write( '<td><a href="#" title="'+_i+'" data-value="'+_i+'">'+_i+'</a></td>' ); 
							}
						}
						document.write('</tr>');    
					</script>
				</table>
			</div>
		</div>
	</div>
</body>
</html>