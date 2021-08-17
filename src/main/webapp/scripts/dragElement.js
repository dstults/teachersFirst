
// Original version from:
// https://www.w3schools.com/howto/howto_js_draggable.asp

const dragElement = (clickedElement, draggedElement) => {
	let pos1 = 0;
	let pos2 = 0;
	let pos3 = 0;
	let pos4 = 0;

	const dragMouseDown = (e) => {
		e = e || window.event;
		e.preventDefault();
		// get the mouse cursor position at startup:
		pos3 = e.clientX;
		pos4 = e.clientY;
		document.onmouseup = closeDragElement;
		// call a function whenever the cursor moves:
		document.onmousemove = elementDrag;
	};

	const elementDrag = (e) => {
		e = e || window.event;
		e.preventDefault();
		// calculate the new cursor position:
		pos1 = pos3 - e.clientX;
		pos2 = pos4 - e.clientY;
		pos3 = e.clientX;
		pos4 = e.clientY;
		// Make sure element is within bounds
		let newTop = draggedElement.offsetTop - pos2;
		if (newTop < 3) newTop = 3;
		if (newTop > window.innerHeight - draggedElement.offsetHeight) newTop = window.innerHeight - draggedElement.offsetHeight;

		let newLeft = draggedElement.offsetLeft - pos1;
		if (newLeft < 3) newLeft = 3;
		if (newLeft > window.innerWidth - draggedElement.offsetWidth) newLeft = window.innerWidth - draggedElement.offsetWidth;
		// Set new position:
		draggedElement.style.top = newTop + 'px';
		draggedElement.style.left = newLeft + 'px';
	};

	const closeDragElement = _ => {
		// stop moving when mouse button is released:
		document.onmouseup = null;
		document.onmousemove = null;
	};

	clickedElement.onmousedown = dragMouseDown;

};
