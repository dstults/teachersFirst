<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Calendar</title>
    <link rel="stylesheet" href="/css/main.css">
</head>

<body>
    <header>
        <div class="landing-text">
            <h1>Name of the website</h1>
            <h6>Very short one sentence description</h6>
        </div>
        <nav class="top-nav">
            <ul class="nav-list">
                <li>
                    <a href="/services" class="nav-link">Services</a>
                </li>
                <li>
                    <a href="/messages" class="nav-link">Messages</a>
                </li>
                <li>
                    <a href="/appointments" class="nav-link">Appointments</a>
                </li>
                <li>
                    <a href="/calendar" class="nav-link">Calendar</a>
                </li>
                <li>
                    <a href="/profile" class="nav-link">Profile</a>
                </li>
            </ul>
        </nav>
    </header>

    <div class="calendar-explanation">
        <p>That's the calendar of all your scheduled appointments</p>
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