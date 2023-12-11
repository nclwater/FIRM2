from xml.sax.saxutils import escape
from StringIO import StringIO

def islist(x):
	"""return True iff x is iterable and not a string"""
	if isinstance(x, basestring):
		return False
	try:
		iter(x)
	except:
		return False
	else:
		return True

def make_attrs(dct):
	"""Turn a dict into string of XML attributes."""
	return u"".join((u' %s="%s"' % (x, escape(unicode(y), {'"': '&quot;'})) 
		for x, y in dct.iteritems()))

def isleaf(x):
	"""Return true if node x definitely does not contain any tree subelements"""
	if isinstance(x, basestring):
		return True
	return not islist(x) and not isinstance(x, tuple)

def writexml(node, level, env):
	"""Write XML representation of one node.
	Level gives the current number of indents, and env holds
	values that are constant through the whole computation:
	env['indent'] holds a string for one level of indent.
	env['nl'] holds the newline character (or nothing if not printing prettily)
	env['write'] holds a function that writes an object to the output file"""

	indent = env['indent'] * level
	write = env['write']
	nl = env['nl']
	if isinstance(node, tuple):
		if len(node) < 2:
			error
		name = node[0]
		attrs = {}
		contents = None
		if not isinstance(name, basestring):
			error
		if len(node) == 2:
			if isinstance(node[1], dict):
				attrs = node[1]
			else:
				contents = node[1]
		elif len(node) == 3:
			attrs = node[1]
			contents = node[2]
		else:
			raise PyfoError('invalid tuple')
		if callable(contents):
			contents = contents()
		if not isinstance(attrs, dict):
			raise PyfoError('attributes are not a dictionary')
		if isleaf(contents):
			if contents == None:
				contents = ''
			else:
				contents = escape(unicode(contents))
			if contents == '' and env['collapse']:
				write(u"%s<%s%s/>%s" % (indent, name, make_attrs(attrs), nl))
			else:
				write(u"%s<%s%s>" % (indent, name, make_attrs(attrs)))
				write(contents)
				write(u"</%s>%s" % (name, nl))
		else:
			write("%s<%s%s>%s" % (indent, name, make_attrs(attrs), nl))
			writexml(contents, level+1, env)
			write("%s</%s>%s" % (indent, name, nl))
	elif islist(node):
		for c in node:
			if c:
				if isleaf(c):
					write(indent)
					write(unicode(c))
					write(nl)
				else:
					writexml(c, level, env)
	elif isinstance(node, basestring):
		write(indent)
		write(unicode(node))
		write(nl)
	elif callable(node):
		writexml(node(), level, env)
	else:
		raise PyfoError('invalid kind of element')

def pyfo(node,
		file=None,
		prolog=False, 
		pretty=False, 
		indent_size=2, 
		encoding='utf-8', 
		collapse=True):
	"""Write XML represented by node.
	
	If prolog is True (default False), write the XML header.
	
	if pretty is True (default False), indent XML nicely,
	
	in which case indent_size gives the number of spaces at each indent level.
	
	File holds a file-like object (supporting write) to which the XML will
	be written. If it is None (the default), a unicode string holding the XML will be
	returned instead.
	
	The character encoding is given by encoding; this goes in the prolog. If
	file is not None, the encoding is also performed on the XML before being
	written to the file.
	
	The collapse is True (default True), <foo></foo> will be collapsed into <foo/>.

	If node is callable, it is called, and the result used in place of node.

	If node is a tuple, it may have two or three elements
		(name, [attrs, ] contents)
	Name gives the element name.
	Attrs (if given) is a dictionary holding attributes and values associated with the element.
	If contents is a string and not iterable, it is quoted to become the contents of the element,
	otherwise pyfo is called recursively on it.

	If node is a string, it holds literal XML.
	If node is iterable, pyfo is called recursively on each element,
	and the results concatenated.
	"""
	if file is None:
		f = StringIO()
		write = lambda x, f = f: f.write(x)
	else:
		f = file
		write = lambda x, f = f, encoding = encoding, : f.write(x.encode(encoding, 'xmlcharrefreplace'))

	if prolog:
		write(u'<?xml version="1.0" encoding="%s"?>\n' % encoding)

	env = dict(pretty=pretty, collapse = collapse, write = write)
	if pretty:
		env['nl'] = '\n'
		env['indent'] = ' ' * indent_size
	else:
		env['nl'] = ''
		env['indent'] = ''
	writexml(node, 0, env)
	if file is None:
		return f.getvalue()
	else:
		return None

